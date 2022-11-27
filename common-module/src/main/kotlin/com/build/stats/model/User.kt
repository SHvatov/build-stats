package com.build.stats.model

import com.build.stats.model.aware.TokenAware
import com.build.stats.model.converter.JsonAttributeConverter
import com.build.stats.utils.RequiredProperty
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Converter
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Transient

private const val TABLE_NAME = "gh_user"


@Entity
@Table(name = TABLE_NAME)
class User @JvmOverloads constructor(
    id: Long? = null,
    username: String? = null,
    email: String? = null,
    token: String? = null,
    config: UserConfig? = null
) : TokenAware {
    @field:Id
    @field:Column(name = "id")
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    private var _id: Long? = id

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var id: Long by RequiredProperty<User, Long>(this::_id, mandatory = true)

    @field:Column(name = "username")
    private var _username: String? = username

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var username: String by RequiredProperty<User, String>(this::_username, mandatory = true)

    @field:Column(name = "email")
    private var _email: String? = email

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var email: String by RequiredProperty<User, String>(this::_email, mandatory = true)

    @field:Column(name = "token")
    private var _token: String? = token

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    override var token: String by RequiredProperty<User, String>(this::_token) { TokenAware.EMPTY_TOKEN }

    @field:Column(name = "config")
    @field:Convert(converter = UserConfigConverter::class)
    private var _config: UserConfig? = config

    @delegate:Transient
    @Suppress("JpaAttributeTypeInspection")
    var config: UserConfig by RequiredProperty<User, UserConfig>(this::_config) { UserConfig() }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserConfig @JsonCreator constructor(
    @JsonProperty("chatId") val chatId: String? = null
)

@Converter(autoApply = true)
class UserConfigConverter : JsonAttributeConverter<UserConfig>()
