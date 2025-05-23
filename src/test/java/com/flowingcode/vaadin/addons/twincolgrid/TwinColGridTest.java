package com.flowingcode.vaadin.addons.twincolgrid;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedHashSet;

public class TwinColGridTest {

    @Test
    public void testGetValueReturnsModifiableSet() {
        // 1. Create an instance of TwinColGrid<String>
        TwinColGrid<String> twinColGrid = new TwinColGrid<>();

        // 2. Add a couple of items to the selection side of the grid.
        //    To do this, we first set items to the available side,
        //    then simulate moving them to the selection side.
        //    Alternatively, setValue directly manipulates the selection.
        Set<String> initialSelection = new LinkedHashSet<>(Arrays.asList("Dasher", "Dancer"));
        twinColGrid.setValue(initialSelection);

        // 3. Call the getValue() method to get the set of selected items.
        Set<String> selectedItems = twinColGrid.getValue();

        // 4. Assert that the returned set is not null and contains the expected items.
        Assert.assertNotNull("The set returned by getValue() should not be null.", selectedItems);
        Assert.assertEquals("The set should contain 2 items.", 2, selectedItems.size());
        Assert.assertTrue("The set should contain 'Dasher'.", selectedItems.contains("Dasher"));
        Assert.assertTrue("The set should contain 'Dancer'.", selectedItems.contains("Dancer"));

        // 5. Attempt to add a new item (e.g., "Prancer") to the set returned by getValue().
        boolean added = false;
        try {
            added = selectedItems.add("Prancer");
        } catch (UnsupportedOperationException e) {
            Assert.fail("The set should be modifiable, but add operation threw UnsupportedOperationException.");
        }

        // 6. Assert that the add operation was successful and the set now contains the newly added item.
        Assert.assertTrue("The add operation should return true, indicating the set was modified.", added);
        Assert.assertEquals("The set should now contain 3 items.", 3, selectedItems.size());
        Assert.assertTrue("The set should contain 'Prancer'.", selectedItems.contains("Prancer"));

        // 7. Call getValue() on the TwinColGrid instance again to get a fresh set.
        Set<String> internalSelectionAfterModification = twinColGrid.getValue();

        // 8. Assert that this new set contains only the original items and does not contain "Prancer".
        Assert.assertNotNull("The internal set fetched after modification should not be null.", internalSelectionAfterModification);
        Assert.assertEquals("The internal set should still contain 2 original items.", 2, internalSelectionAfterModification.size());
        Assert.assertTrue("The internal set should still contain 'Dasher'.", internalSelectionAfterModification.contains("Dasher"));
        Assert.assertTrue("The internal set should still contain 'Dancer'.", internalSelectionAfterModification.contains("Dancer"));
        Assert.assertFalse("The internal set should NOT contain 'Prancer'.", internalSelectionAfterModification.contains("Prancer"));
    }
}
