package cl.saraos.bank.controllers

import cl.saraos.bank.entity.UserEntity
import cl.saraos.bank.repository.PhoneRepository
import cl.saraos.bank.repository.UserRepository
import cl.saraos.bank.service.JwtTokenService
import cl.saraos.bank.service.RegisterService
import cl.saraos.bank.service.UserService
import groovy.json.JsonSlurper
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.mockito.ArgumentMatchers
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

@WebMvcTest(RegisterController)
@ComponentScan("cl.saraos.bank")
class RegisterControllerSpec extends Specification {

    MockMvc mockMvc

    @Autowired
    WebApplicationContext webApplicationContext

    @Autowired
    private RegisterService registerService

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    UserService userService

    @Autowired
    JwtTokenService jwtTokenService

    @SpringBean
    UserRepository userRepository = Mock()

    @SpringBean
    PhoneRepository phoneRepository = Mock()

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    def "test regiter api ok"() {
        given:

        def request = """
{
    "name": "name",
    "email": "email@aaa.cl",
    "password": "Aa12bbvvfg",
    "phones": [
        {
            "number": 122344,
            "citycode": 11,
            "contrycode": "CL"
        }
    ]
}
"""

        def jsonExpectResponse = """ {
    "id": 3,
    "created": "ago 03, 2023 01:58:24 PM",
    "lastLogin": "ago 03, 2023 01:58:24 PM",
    "token": "",
    "isActive": true,
    "name": "name",
    "email": "email@aaa.cl",
    "password": "",
    "phones": [
        {
            "number": 122344,
            "citycode": 11,
            "contrycode": "CL"
        }
    ]
}"""

        def expectResponse = new JsonSlurper().parseText(jsonExpectResponse)
        1 * userRepository.save(_) >> { UserEntity entity ->
            entity
        }
        userRepository.findAllByEmail(_) >> Arrays.asList()
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/sign-up").
                contentType(MediaType.APPLICATION_JSON).
                content(request)
        ).andReturn().response

        then:
        result.status == HttpStatus.OK.value()
        result.contentType == MediaType.APPLICATION_JSON_VALUE

        and:
        def responseJson = new JsonSlurper().parseText(result.contentAsString)
        responseJson.name == expectResponse.name
        responseJson.email == expectResponse.email
        Assert.assertNotNull(responseJson.phones)
    }


    def "test regiter user exist"() {
        given:

        def request = """
{
    "name": "name",
    "email": "email@aaa.cl",
    "password": "Aa12bbvvfg",
    "phones": [
        {
            "number": 122344,
            "citycode": 11,
            "contrycode": "CL"
        }
    ]
}
"""

        def jsonExpectResponse = """ {
    "id": 3,
    "created": "ago 03, 2023 01:58:24 PM",
    "lastLogin": "ago 03, 2023 01:58:24 PM",
    "token": "",
    "isActive": true,
    "name": "name",
    "email": "email@aaa.cl",
    "password": "",
    "phones": [
        {
            "number": 122344,
            "citycode": 11,
            "contrycode": "CL"
        }
    ]
}"""

        def expectResponse = new JsonSlurper().parseText(jsonExpectResponse)
        0 * userRepository.save(_) >> { UserEntity entity ->
            entity
        }
        1 * userRepository.findAllByEmail(_) >> Arrays.asList(
                UserEntity.builder().build()
        )
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/sign-up").
                contentType(MediaType.APPLICATION_JSON).
                content(request)
        ).andReturn().response

        then:
        result.status == HttpStatus.FORBIDDEN.value()
        result.contentType == MediaType.APPLICATION_JSON_VALUE

        and:
        def responseJson = new JsonSlurper().parseText(result.contentAsString)
        Assert.assertNotNull(responseJson.error)
        responseJson.error.size > 0

    }



    def "test regiter bad value password"() {
        given:

        def request = """
{
    "name": "name",
    "email": "email@aaa.cl",
    "password": "Aa12bbvv1fg",
    "phones": [
        {
            "number": 122344,
            "citycode": 11,
            "contrycode": "CL"
        }
    ]
}
"""

        def jsonExpectResponse = """ {
    "id": 3,
    "created": "ago 03, 2023 01:58:24 PM",
    "lastLogin": "ago 03, 2023 01:58:24 PM",
    "token": "",
    "isActive": true,
    "name": "name",
    "email": "email@aaa.cl",
    "password": "",
    "phones": [
        {
            "number": 122344,
            "citycode": 11,
            "contrycode": "CL"
        }
    ]
}"""

        def expectResponse = new JsonSlurper().parseText(jsonExpectResponse)
        0 * userRepository.save(_) >> { UserEntity entity ->
            entity
        }
        1 * userRepository.findAllByEmail(_) >> Arrays.asList(
                UserEntity.builder().build()
        )
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/sign-up").
                contentType(MediaType.APPLICATION_JSON).
                content(request)
        ).andReturn().response

        then:
        result.status == HttpStatus.BAD_REQUEST.value()
        result.contentType == MediaType.APPLICATION_JSON_VALUE

        and:
        def responseJson = new JsonSlurper().parseText(result.contentAsString)
        Assert.assertNotNull(responseJson.error)
        responseJson.error.size > 0
    }


    def "test regiter api correo invalido"() {
        given:

        def request = """
{
    "name": "name",
    "email": "emailaaa.cl",
    "password": "Aa12bbvvfg",
    "phones": [
        {
            "number": 122344,
            "citycode": 11,
            "contrycode": "CL"
        }
    ]
}
"""

        def jsonExpectResponse = """ {
    "id": 3,
    "created": "ago 03, 2023 01:58:24 PM",
    "lastLogin": "ago 03, 2023 01:58:24 PM",
    "token": "",
    "isActive": true,
    "name": "name",
    "email": "email@aaa.cl",
    "password": "",
    "phones": [
        {
            "number": 122344,
            "citycode": 11,
            "contrycode": "CL"
        }
    ]
}"""

        def expectResponse = new JsonSlurper().parseText(jsonExpectResponse)
        0 * userRepository.save(_) >> { UserEntity entity ->
            entity
        }
        0 * userRepository.findAllByEmail(_) >> Arrays.asList()
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/sign-up").
                contentType(MediaType.APPLICATION_JSON).
                content(request)
        ).andReturn().response

        then:
        result.status == HttpStatus.BAD_REQUEST.value()
        result.contentType == MediaType.APPLICATION_JSON_VALUE

        and:
        def responseJson = new JsonSlurper().parseText(result.contentAsString)
        Assert.assertNotNull(responseJson.error)
        responseJson.error.size > 0
    }



    def "test regiter api parametros invalidos"() {
        given:

        def request = """
{
    "name": "name",
    "email": "emailaaa.cl",
    "password": "Aa12bbvAvfg",
    "phones": [
        {
            "number": 122344,
            "citycode": 11,
            "contrycode": "CL"
        }
    ]
}
"""

        def jsonExpectResponse = """ {
    "id": 3,
    "created": "ago 03, 2023 01:58:24 PM",
    "lastLogin": "ago 03, 2023 01:58:24 PM",
    "token": "",
    "isActive": true,
    "name": "name",
    "email": "email@aaa.cl",
    "password": "",
    "phones": [
        {
            "number": 122344,
            "citycode": 11,
            "contrycode": "CL"
        }
    ]
}"""

        def expectResponse = new JsonSlurper().parseText(jsonExpectResponse)
        0 * userRepository.save(_) >> { UserEntity entity ->
            entity
        }
        0 * userRepository.findAllByEmail(_) >> Arrays.asList()
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/sign-up").
                contentType(MediaType.APPLICATION_JSON).
                content(request)
        ).andReturn().response

        then:
        result.status == HttpStatus.BAD_REQUEST.value()
        result.contentType == MediaType.APPLICATION_JSON_VALUE

        and:
        def responseJson = new JsonSlurper().parseText(result.contentAsString)
        Assert.assertNotNull(responseJson.error)
        responseJson.error.size == 2
    }
}
