package com.endava.internship.collections;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.time.LocalDate;

public class StudentSetTest {
    StudentSet set;


    @BeforeEach
    void setUpStudentSet(){
        set = new StudentSet();
    }

    @Test
    void addElementShouldReturnTrue(){
        Student st = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        assertTrue(set.add(st));
    }

    @Test
    void addDuplicatedNullToSetShouldAllowASingleNullElement(){
        set.add(null);
        set.add(null);
        assertThat(set).containsNull();
    }

    @Test
    void addDublicatedObjectToSetShouldNotBeAdded(){
        Student st = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        set.add(st);
        assertAll(
                () -> assertFalse(set.add(st)),
                () -> assertThat(set).containsOnlyOnce(st)
        );
    }

    @Test
    void addNullToSetShouldBeAdded(){
        assertTrue(set.add(null));
    }

    @Test
    void setShouldBeEmptyIfNoContainsElements(){
        assertThat(set).isEmpty();
    }

    @Test
    void sizeShouldBe0IfNoContainsElements(){
        assertEquals(0, set.size());
    }

    @Test
    void ifElementIsContainedItShouldReturnTrue(){
        Student st = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        set.add(st);
        assertTrue(set.contains(st));
    }

    @Test
    void ifElementIsNotContainedItShouldReturnFalse(){
        Student st = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        assertFalse(set.contains(st));
    }

    @Test
    void setShouldBeClearedIfContainsElements(){
        set.add(new Student("John Doe", LocalDate.of(2000, 5, 12), ""));
        set.clear();

        assertAll(
                () -> assertThat(set).isEmpty(),
                () -> assertThat(set).hasSize(0)
        );
    }

    @Test
    void elementShoulBeRemovedFromSetIfItIsContained(){
        Student st = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        set.add(st);

        assertAll(
                () -> assertTrue(set.remove(st)),
                () -> assertTrue(!set.contains(st))
        );
    }

    @Test
    void elementShouldBeNotRemovedFromSetIfItIsNotContained(){
        Student st = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        assertFalse(set.remove(st));
    }

    @Test
    void removeNullElementThatIsNotContainedShouldReturnFalse(){
        assertFalse(set.remove(null));
    }

    @Test
    void nullElementShouldBeRemovedIfItIsContained(){
        set.add(null);

        set.remove(null);
        assertFalse(set.contains(null));
    }

    @Test
    void methodShouldReturnIteratorThatContainsNotNullElementsFromTheSet(){
        Student st1 = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        Student st2 = new Student("David", LocalDate.of(2001, 6, 20), "");

        addElementsToSet(set, st1, st2);
        Iterator it = set.iterator();
        while(it.hasNext()) {
            assertNotNull(it.next());
        }
    }

    @Test
    void elementsFromOtherCollectionShouldBeAddedInExactlyOrder(){
        Student st1 = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        Student st2 = new Student("David", LocalDate.of(2001, 6, 20), "");

        List<Student> list = Arrays.asList(st1, null, st2);

        set.addAll(list);
        assertThat(set).containsExactlyInAnyOrder(st1, null, st2);
    }

    @Test
    void mustRemoveAllElementsContainedInTheOtherCollection(){
        Student st1 = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        Student st2 = new Student("David", LocalDate.of(2001, 6, 20), "");

        addElementsToSet(set, st1, st2);

        List<Student> list = Arrays.asList(st1, null, st2);

        set.removeAll(list);
        assertThat(set).doesNotContainAnyElementsOf(list);
    }

    @Test
    void removeElementsShouldReturnNullPointerExceptionIfCollectionIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> set.removeAll(null));
    }

    @Test
    void shouldReturnTrueIfAllElementsFromOtherCollectionIsContainedInSetInAnyPosition(){
        Student st1 = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        Student st2 = new Student("David", LocalDate.of(2001, 6, 20), "");

        addElementsToSet(set, st1, st2, null);

        List<Student> list = Arrays.asList(st1, null);

        assertTrue(set.containsAll(list));
    }

    @Test
    void retainElementsShouldReturnNullPointerExceptionIfCollectionIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> set.retainAll(null));
    }

    @Test
    void mustRetainAllElementsContainedInTheOtherCollection(){
        Student st1 = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        Student st2 = new Student("David", LocalDate.of(2001, 6, 20), "");

        addElementsToSet(set, st1, st2, null);

        List<Student> list = Arrays.asList(st1, null);

        set.retainAll(list);
        assertThat(set).containsOnlyElementsOf(list);
    }

    @Test
    void ifTheArrayIsLargerThanTheCollectionItShouldReturnAnArrayOfTheObjectWithTheSameSize(){
        Student st1 = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        Student st2 = new Student("David", LocalDate.of(2001, 6, 20), "");

        addElementsToSet(set, st1, st2, null);
        Student[] students = set.toArray(new Student[5]);

        assertAll(
                () -> assertThat(students).hasSize(5),
                () -> assertThat(students).containsAll(set)
        );
    }

    @Test
    void ifTheArrayIsSmallerThanTheCollectionItShouldReturnAnArrayOfTheObjectWithTheSameSizeAsSet(){
        Student st1 = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        Student st2 = new Student("David", LocalDate.of(2001, 6, 20), "");

        addElementsToSet(set, st1, st2, null);
        Student[] students = set.toArray(new Student[1]);

        assertAll(
                () -> assertThat(students).hasSize(3),
                () -> assertThat(students).containsAll(set)
        );
    }

    @Test
    void elementsInTheArrayShouldBeInTheSameOrderAsInBuckets(){
        Student st1 = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        Student st2 = new Student("David", LocalDate.of(2001, 6, 20), "");

        addElementsToSet(set, null, st1, st2);
        Object[] students = set.toArray();
        Object[] path = new Object[]{null, st2, st1};

        assertArrayEquals(students, path);
    }

    @Test
    void setShouldBeResizedIfSizeIsLargerThenLoadFactor(){
        set = new StudentSet(1);

        Student st1 = new Student("John Doe", LocalDate.of(2000, 5, 12), "");
        Student st2 = new Student("David", LocalDate.of(2001, 6, 20), "");
        Student st3 = new Student("Dmitrii", LocalDate.of(1996, 10, 16), "");

        addElementsToSet(set, st1, st2, st3);

        assertThat(set).hasSize(3);
    }


    private void addElementsToSet(StudentSet set, Student ... students){
        for(Student s : students){
            set.add(s);
        }
    }
}
