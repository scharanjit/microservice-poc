package people

import grails.gorm.services.Service
import grails.gorm.services.Where
import groovy.transform.CompileStatic

@Service(Person)
@CompileStatic
abstract class PersonService {
    abstract int count()

    abstract Person get(long id)

    abstract Person savePerson(String firstName,
                               String lastName,
                               int age)

    abstract Person savePerson(String firstName,
                               String lastName,
                               int age,
                               boolean enabled)

    abstract List<Person> list()

    @Where({ enabled == true })
    abstract List<Person> listEnabled()

    @Where({ enabled == false })
    abstract List<Person> listDisabled()

    abstract Person save(Person person)

    Person disable(Person person) {
        person.enabled = false
        save person
    }

    Person enable(Person person) {
        person.enabled = true
        save person
    }
}
