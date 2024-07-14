package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    public void shouldBeEqualsWithSameId() {
        Epic epic1 = new Epic("Epic1", "Description1");
        Epic epic2 = new Epic("Epic2", "Description2");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2);
    }

}