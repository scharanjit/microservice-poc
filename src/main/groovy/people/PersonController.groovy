package people

import groovy.transform.CompileStatic
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.validation.Validated

import javax.inject.Inject
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern

@Controller('/people')
@CompileStatic
@Validated
class PersonController {

    @Inject
    PersonService personService

    @Get('/')
    List<Person> list() {
        personService.list()
    }

    @Get('/{id}')
    Person person(long id) {
        personService.get id
    }

    @Post('/')
    Person save(@Pattern(regexp = '^[A-Z].*') String firstName,
                @Pattern(regexp = '^[A-Z].*') String lastName,
                @Min(0l) int age) {
        personService.savePerson firstName, lastName, age
    }

    @Get('/enabled')
    List<Person> enabledPeople() {
        personService.listEnabled()
    }

    @Get('/disabled')
    List<Person> disabledPeople() {
        personService.listDisabled()
    }

    @Put('/{id}/enable')
    HttpResponse<Person> enable(long id) {
        Person person = personService.get id
        if(!person) {
            null
        } else if(person.enabled) {
            HttpResponse.status(HttpStatus.BAD_REQUEST)
        } else {
            HttpResponse.ok(personService.disable(person))
        }
    }

    @Put('/{id}/disable')
    HttpResponse<Person> disable(long id) {
        Person person = personService.get id
        if(!person) {
            null
        } else if(!person.enabled) {
            HttpResponse.status(HttpStatus.BAD_REQUEST)
        } else {
            HttpResponse.ok(personService.disable(person))
        }
    }
}
