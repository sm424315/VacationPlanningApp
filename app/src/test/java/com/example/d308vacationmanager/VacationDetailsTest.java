package com.example.d308vacationmanager;

import org.junit.Assert;
import org.junit.Test;

import com.smcferro.d308vacationmanager.Entities.Vacation;

public class VacationDetailsTest {
    @Test
    public void testValidVacation() { /// Tests format to make sure it adheres to our class structure
        Vacation vacation = new Vacation(1, "Beach Holiday", "Hotel XYZ", "06/01/2025", "06/07/2025");
        Assert.assertTrue(vacation.isValid());
    }

    @Test
    public void testEmptyTitle() { /// Makes sure the isValid data validation function catches empty titles
        Vacation vacation = new Vacation(1, "", "Hotel XYZ", "06/01/2025", "06/07/2025");
        Assert.assertFalse(vacation.isValid());
    }

    @Test
    public void testInvalidDate() { /// Makes sure the isValid data validation function catches invalid dates
        Vacation vacation = new Vacation(1, "Beach Holiday", "Hotel XYZ", "06/01/2025", "06/40/2025");
        Assert.assertFalse(vacation.isValid());
    }

    @Test
    public void testStartDateAfterEndDate() { /// Makes sure the isValid data validation function catches dates that end before the start date
        Vacation vacation = new Vacation(1, "Beach Holiday", "Hotel XYZ", "06/08/2025", "06/07/2025");
        Assert.assertFalse(vacation.isValid());
    }
}
