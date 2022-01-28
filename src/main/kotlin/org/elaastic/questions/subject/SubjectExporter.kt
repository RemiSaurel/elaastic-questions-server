package org.elaastic.questions.subject

import com.fasterxml.jackson.databind.ObjectMapper
import org.elaastic.questions.attachment.AttachmentService
import org.elaastic.questions.directory.User
import org.elaastic.questions.util.ZipService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Service
import java.io.*
import java.util.*
import java.util.zip.ZipFile

@Service
class SubjectExporter(
    // Inject that bean to get the mapper used by Spring MVC (that is properly configured)
    @Qualifier("mappingJackson2HttpMessageConverter") @Autowired springMvcJacksonConverter: MappingJackson2HttpMessageConverter,
    @Autowired val zipService: ZipService,
    @Autowired val attachmentService: AttachmentService,
    @Autowired val subjectService: SubjectService,
) {

    val mapper: ObjectMapper = springMvcJacksonConverter.objectMapper

    companion object {
        const val RESOURCE_FOLDER = "resource"
    }


    /**
     * Export the subject (including statements, attachments & fake answers) to a POJO
     */
    fun exportToPojo(subject: Subject) =
        ExportSubjectData(subject)

    /**
     * Export the subject to JSON (which does not include attachments)
     */
    fun exportToJson(subject: Subject): String = exportToJson(exportToPojo(subject))

    /**
     * Export the subject to JSON (which does not include attachments)
     */
    fun exportToJson(exportSubjectData: ExportSubjectData): String =
        mapper.writeValueAsString(exportSubjectData)

    /**
     * Export the subject to a ZIP archive include a JSON file for the subject data and 1 file for each
     * attachments
     */
    fun exportToZip(subject: Subject, filename: String, outputStream: OutputStream) {
        val exportSubjectData = exportToPojo(subject)

        zipService.zip(
            outputStream,
            listOf(
                ZipService.ZipEntryData(
                    "${filename}.elaastic.json",
                    exportToJson(exportSubjectData).byteInputStream()
                )

            ) +
                    exportSubjectData.getAttachmentList().map { exportAttachment ->
                        ZipService.ZipEntryData(
                            "${RESOURCE_FOLDER}/${exportAttachment.path}",
                            attachmentService.getInputStreamForPath(exportAttachment.path)
                        )
                    }
        )
    }

    /**
     * Extract Subject data from JSON
     */
    fun parseFromJson(jsonReader: Reader): ExportSubjectData =
        mapper.readValue(jsonReader, ExportSubjectData::class.java)

    /**
     * Import a subject from JSON
     */
    fun importFromJson(user: User, jsonReader: Reader): Subject =
        subjectService.createFromExportData(
            user,
            parseFromJson(jsonReader)
        )

    /**
     * Import a subject and its attachments from a ZIP archive
     */
    fun importFromZip(user: User, inputStream: InputStream): Subject {
        val zip = File.createTempFile(UUID.randomUUID().toString(), null)

        inputStream.use { input ->
            zip.outputStream().use { output ->
                input.copyTo(output, 16 * 1024)
            }
        }

        val extractedFiles = zipService.unzip(ZipFile(zip))

        // Extract the subject data
        val exportSubjectData = parseFromJson(
            InputStreamReader(extractedFiles.first().file.inputStream())
        )
        // Inject assignments
        exportSubjectData.getAttachmentList().forEach { exportAttachment ->
            exportAttachment.attachmentFile = extractedFiles.find { it.name == "${RESOURCE_FOLDER}/"+exportAttachment.path }?.file
        }

        // Import the subject
        val subject = subjectService.createFromExportData(
            user,
            exportSubjectData
        )

        // clean up all tmp files
        zip.delete()
        extractedFiles.forEach { it.file.delete() }

        return subject
    }


}