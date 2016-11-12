package org.tiatus.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class ClubTest {
    @Test
    public void testClubId() {
        Club club = new Club();
        club.setId(1L);
        Assert.assertEquals(club.getId(), Long.valueOf(1L));
    }

    @Test
    public void testClubName() {
        Club club = new Club();
        club.setClub("name");
        Assert.assertEquals(club.getClub(), "name");
    }

    @Test
    public void testClubEqualsSameInstance() {
        Club club = new Club();
        Assert.assertTrue(club.equals(club));
    }

    @Test
    public void testClubEqualsDifferentType() {
        Club club = new Club();
        Assert.assertFalse(club.equals(new User()));
    }

    @Test
    public void testClubEqualsDifferentInstance() {
        Club club = new Club();
        club.setId(1L);
        club.setClub("name");
        Club club2 = new Club();
        club2.setId(1L);
        club2.setClub("name");
        Assert.assertTrue(club.equals(club2));
    }

    @Test
    public void testClubHashCode() {
        Club club = new Club();
        club.setId(1L);
        club.setClub("name");
        Club club2 = new Club();
        club2.setId(1L);
        club2.setClub("name");
        Assert.assertEquals(club.hashCode(), club2.hashCode());
    }
}