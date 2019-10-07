/*
 * Elaastic - formative assessment system
 * Copyright (C) 2019. University Toulouse 1 Capitole, University Toulouse 3 Paul Sabatier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elaastic.questions.directory

import org.elaastic.questions.persistence.AbstractJpaPersistable
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.core.GrantedAuthority
import java.io.Serializable
import javax.persistence.*



@Entity
@Cacheable("roles")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
class Role(
        @field:Column(name = "authority")
        var name: String
) : AbstractJpaPersistable<Long>(), Serializable, GrantedAuthority {

    enum class RoleId(val roleName: String) {
        STUDENT("STUDENT_ROLE"),
        TEACHER("TEACHER_ROLE"),
        ADMIN("ADMIN_ROLE")
    }

    override fun getAuthority(): String {
        return name
    }
}
