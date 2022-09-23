package com.endava.internship.collections;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.time.LocalDate;

public class StudentSetTest {
    private StudentSet set;

    private final static Student st1 = new Student("John", LocalDate.of(2000, 5, 12), "");

    private final static Student st2 = new Student("David", LocalDate.of(2001, 6, 20), "");

    private final static Student st3 = new Student("Dmitrii", LocalDate.of(1996, 10, 16), "");

    @BeforeEach
    void setUpStudentSet(){
        set = new StudentSet();
    }

    @Test
    void addElementShouldReturnTrue(){
        addElementsToSet(set, st1);

        assertAll(
                () -> assertThat(set).isNotEmpty(),
                () -> assertThat(set).hasSize(1)
        );
    }

    @Test
    void addDuplicatedNullToSetShouldAllowASingleNullElement(){
        assertTrue(set.add(null));
        assertFalse(set.add(null));

        assertAll(
                () -> assertThat(set).hasSize(1),
                () -> assertThat(set).containsNull()
        );
    }

    @Test
    void addDublicatedObjectToSetShouldNotBeAdded(){
        addElementsToSet(set, st1);

        assertAll(
                () -> assertFalse(set.add(st1)),
                () -> assertThat(set).containsOnlyOnce(st1)
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
        addElementsToSet(set, st1);
        assertTrue(set.contains(st1));
    }

    @Test
    void ifElementIsNotContainedItShouldReturnFalse(){
        assertFalse(set.contains(st1));
    }

    @Test
    void setShouldBeClearedIfContainsElements(){
        addElementsToSet(set, st1);
        set.clear();

        assertAll(
                () -> assertThat(set).isEmpty(),
                () -> assertThat(set).hasSize(0)
        );
    }

    @Test
    void elementShoulBeRemovedFromSetIfItIsContained(){
        assertTrue(set.add(st1));

        assertAll(
                () -> assertTrue(set.remove(st1)),
                () -> assertTrue(!set.contains(st1))
        );
    }

    @Test
    void elementShouldBeNotRemovedFromSetIfItIsNotContained(){
        assertFalse(set.remove(st1));
    }

    @Test
    void removeNullElementThatIsNotContainedShouldReturnFalse(){
        assertFalse(set.remove(null));
    }

    @Test
    void nullElementShouldBeRemovedIfItIsContained(){
        assertTrue(set.add(null));
        assertTrue(set.remove(null));
        assertFalse(set.contains(null));
    }

    @Test
    void methodShouldReturnIteratorThatContainsNotNullElementsFromTheSet(){
        addElementsToSet(set, st1, st2);
        Iterator it = set.iterator();
        while(it.hasNext()) {
            assertNotNull(it.next());
        }
    }

    @Test
    void elementsFromOtherCollectionShouldBeAddedInExactlyOrder(){
        List<Student> list = Arrays.asList(st1, null, st2);

        assertTrue(set.addAll(list));
        assertThat(set).containsExactlyInAnyOrder(st1, null, st2);
    }

    @Test
    void mustRemoveAllElementsContainedInTheOtherCollection(){
        addElementsToSet(set, st1, st2);
        List<Student> list = Arrays.asList(st1, null, st2);

        assertTrue(set.removeAll(list));
        assertThat(set).doesNotContainAnyElementsOf(list);
    }

    @Test
    void removeElementsShouldReturnNullPointerExceptionIfCollectionIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> set.removeAll(null));
    }

    @Test
    void shouldReturnTrueIfAllElementsFromOtherCollectionIsContainedInSetInAnyPosition(){
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
        addElementsToSet(set, st1, st2, null);
        List<Student> list = Arrays.asList(st1, null);

        set.retainAll(list);
        assertThat(set).containsOnlyElementsOf(list);
    }

    @Test
    void ifTheArrayIsLargerThanTheCollectionItShouldReturnAnArrayOfTheObjectWithTheSameSize(){
        addElementsToSet(set, st1, st2, null);
        Student[] students = set.toArray(new Student[5]);

        assertAll(
                () -> assertThat(students).hasSize(5),
                () -> assertThat(students).containsAll(set)
        );
    }

    @Test
    void ifTheArrayIsSmallerThanTheCollectionItShouldReturnAnArrayOfTheObjectWithTheSameSizeAsSet(){
        addElementsToSet(set, st1, st2, null);
        Student[] students = set.toArray(new Student[1]);

        assertAll(
                () -> assertThat(students).hasSize(3),
                () -> assertThat(students).containsAll(set)
        );
    }

    @Test
    void elementsInTheArrayShouldBeInTheSameOrderAsInBuckets(){
        addElementsToSet(set, null, st1, st2);
        Object[] students = set.toArray();
        Object[] path = new Object[]{null, st2, st1};

        assertArrayEquals(students, path);
    }

    @Test
    void setShouldBeResizedIfSizeIsLargerThenLoadFactor(){
        set = new StudentSet(1);
        addElementsToSet(set, st1, st2, st3);

        assertThat(set).hasSize(3);
    }


    private void addElementsToSet(StudentSet set, final Student ... students){
        for(Student s : students){
            assertTrue(set.add(s));
        }
    }
}
