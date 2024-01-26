package org.tiatus.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class ClubTest {
    @Test
    public void testClubId() {
        Club club = new Club();
        club.setId(1L);
        Assertions.assertEquals(Long.valueOf(1L), club.getId());
    }

    @Test
    public void testClubName() {
        Club club = new Club();
        club.setClubName("name");
        Assertions.assertEquals("name", club.getClubName());
    }

    @Test
    public void testClubEqualsSameInstance() {
        Club club = new Club();
        Assertions.assertTrue(club.equals(club));
    }

    @Test
    public void testClubEqualsDifferentType() {
        Club club = new Club();
        Assertions.assertFalse(club.equals(new User()));
    }

    @Test
    public void testClubEqualsDifferentInstance() {
        Club club = new Club();
        club.setId(1L);
        club.setClubName("name");
        Club club2 = new Club();
        club2.setId(1L);
        club2.setClubName("name");
        Assertions.assertTrue(club.equals(club2));
    }

    @Test
    public void testClubHashCode() {
        Club club = new Club();
        club.setId(1L);
        club.setClubName("name");
        Club club2 = new Club();
        club2.setId(1L);
        club2.setClubName("name");
        Assertions.assertEquals(club.hashCode(), club2.hashCode());
    }
}