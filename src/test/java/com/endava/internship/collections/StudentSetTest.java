package com.endava.internship.collections;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.time.LocalDate;

public class StudentSetTest {
    private StudentSet set = new StudentSet();;

    private final static Student JOHN = new Student("John", LocalDate.of(2000, 5, 12), "");

    private final static Student DAVID = new Student("David", LocalDate.of(2001, 6, 20), "");

    private final static Student DMITRII = new Student("Dmitrii", LocalDate.of(1996, 10, 16), "");

    @Test
    void addElementShouldReturnTrue(){
        addElementsToSet(set, JOHN);

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
        addElementsToSet(set, JOHN);

        assertAll(
                () -> assertFalse(set.add(JOHN)),
                () -> assertThat(set).containsOnlyOnce(JOHN)
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
        addElementsToSet(set, JOHN);
        assertTrue(set.contains(JOHN));
    }

    @Test
    void ifElementIsNotContainedItShouldReturnFalse(){
        assertFalse(set.contains(JOHN));
    }

    @Test
    void setShouldBeClearedIfContainsElements(){
        addElementsToSet(set, JOHN);
        set.clear();

        assertAll(
                () -> assertThat(set).isEmpty(),
                () -> assertThat(set).hasSize(0)
        );
    }

    @Test
    void elementShoulBeRemovedFromSetIfItIsContained(){
        assertTrue(set.add(JOHN));

        assertAll(
                () -> assertTrue(set.remove(JOHN)),
                () -> assertTrue(!set.contains(JOHN))
        );
    }

    @Test
    void elementShouldBeNotRemovedFromSetIfItIsNotContained(){
        assertFalse(set.remove(JOHN));
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
        addElementsToSet(set, JOHN, DAVID);
        final Iterator it = set.iterator();
        while(it.hasNext()) {
            assertNotNull(it.next());
        }
    }

    @Test
    void elementsFromOtherCollectionShouldBeAddedInExactlyOrder(){
        final List<Student> list = Arrays.asList(JOHN, null, DAVID);

        assertTrue(set.addAll(list));
        assertThat(set).containsExactlyInAnyOrder(JOHN, null, DAVID);
    }

    @Test
    void mustRemoveAllElementsContainedInTheOtherCollection(){
        addElementsToSet(set, JOHN, DAVID);
        final List<Student> list = Arrays.asList(JOHN, null, DAVID);

        assertTrue(set.removeAll(list));
        assertThat(set).doesNotContainAnyElementsOf(list);
    }

    @Test
    void removeElementsShouldReturnNullPointerExceptionIfCollectionIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> set.removeAll(null));
    }

    @Test
    void shouldReturnTrueIfAllElementsFromOtherCollectionIsContainedInSetInAnyPosition(){
        addElementsToSet(set, JOHN, DAVID, null);
        final List<Student> list = Arrays.asList(JOHN, null);

        assertTrue(set.containsAll(list));
    }

    @Test
    void retainElementsShouldReturnNullPointerExceptionIfCollectionIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> set.retainAll(null));
    }

    @Test
    void mustRetainAllElementsContainedInTheOtherCollection(){
        addElementsToSet(set, JOHN, DAVID, null);
        final List<Student> list = Arrays.asList(JOHN, null);

        set.retainAll(list);
        assertThat(set).containsOnlyElementsOf(list);
    }

    @Test
    void ifTheArrayIsLargerThanTheCollectionItShouldReturnAnArrayOfTheObjectWithTheSameSize(){
        addElementsToSet(set, JOHN, DAVID, null);
        final Student[] students = set.toArray(new Student[5]);

        assertAll(
                () -> assertThat(students).hasSize(5),
                () -> assertThat(students).containsAll(set)
        );
    }

    @Test
    void ifTheArrayIsSmallerThanTheCollectionItShouldReturnAnArrayOfTheObjectWithTheSameSizeAsSet(){
        addElementsToSet(set, JOHN, DAVID, null);
        final Student[] students = set.toArray(new Student[1]);

        assertAll(
                () -> assertThat(students).hasSize(3),
                () -> assertThat(students).containsAll(set)
        );
    }

    @Test
    void elementsInTheArrayShouldBeInTheSameOrderAsInBuckets(){
        addElementsToSet(set, null, JOHN, DAVID);
        final Object[] students = set.toArray();
        final Object[] path = new Object[]{null, DAVID, JOHN};

        assertArrayEquals(students, path);
    }

    @Test
    void setShouldBeResizedIfSizeIsLargerThenLoadFactor(){
        set = new StudentSet(1);
        addElementsToSet(set, JOHN, DAVID, DMITRII);

        assertThat(set).hasSize(3);
    }


    private void addElementsToSet(StudentSet set, final Student ... students){
        for(Student s : students){
            set.add(s);
        }
    }
}
