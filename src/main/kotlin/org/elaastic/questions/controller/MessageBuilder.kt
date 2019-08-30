package org.elaastic.questions.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
class MessageBuilder(
        @Autowired val messageSource: MessageSource
) {

    fun success(redirectAttributes: RedirectAttributes,
                message: String) {

        redirectAttributes.addFlashAttribute("messageType", "success")
        redirectAttributes.addFlashAttribute("messageContent", message)
    }

    fun message(code: String, vararg args: String): String {
        return internalMessage(code, arrayOf(*args))
    }

    private fun internalMessage(code: String, args: Array<String>? = null): String {
        return messageSource.getMessage(
                code,
                args,
                LocaleContextHolder.getLocale()
        )
    }
}