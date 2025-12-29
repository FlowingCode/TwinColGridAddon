/*-
 * #%L
 * TwinColGrid add-on
 * %%
 * Copyright (C) 2017 - 2025 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.flowingcode.vaadin.addons.twincolgrid;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TwinColGridListAdapterTest {

    @Test
    public void testGetValueReturnsModifiableListAndDoesNotAffectInternalState() {
        // 1. Instantiate a TwinColGrid<String>
        TwinColGrid<String> twinColGrid = new TwinColGrid<>();

        // 2. Instantiate TwinColGridListAdapter<String> with the created TwinColGrid
        TwinColGridListAdapter<String> adapter = new TwinColGridListAdapter<>(twinColGrid);

        // 3. Add a few initial items (e.g., "Vixen", "Comet") to the TwinColGrid's selection.
        Set<String> initialSelection = new LinkedHashSet<>(Arrays.asList("Vixen", "Comet"));
        twinColGrid.setValue(initialSelection);

        // 4. Call getValue() on the TwinColGridListAdapter instance.
        List<String> listFromAdapter = adapter.getValue();

        // 5. Assert that the returned List<String> is not null and contains the initial items.
        Assert.assertNotNull("The list returned by getValue() should not be null.", listFromAdapter);
        Assert.assertEquals("The list should contain 2 items.", 2, listFromAdapter.size());
        Assert.assertTrue("The list should contain 'Vixen'.", listFromAdapter.contains("Vixen"));
        Assert.assertTrue("The list should contain 'Comet'.", listFromAdapter.contains("Comet"));

        // 6. Attempt to add a new item (e.g., "Cupid") to the list obtained in step 4.
        //    Verify that this operation is successful (no exception thrown) and the list now contains "Cupid".
        boolean added = false;
        try {
            added = listFromAdapter.add("Cupid");
        } catch (UnsupportedOperationException e) {
            Assert.fail("The list should be modifiable, but add operation threw UnsupportedOperationException.");
        }
        Assert.assertTrue("The add operation should return true, indicating the list was modified.", added);
        Assert.assertEquals("The list from adapter should now contain 3 items.", 3, listFromAdapter.size());
        Assert.assertTrue("The list from adapter should now contain 'Cupid'.", listFromAdapter.contains("Cupid"));

        // 7. Call getValue() on the TwinColGridListAdapter again
        List<String> secondListFromAdapter = adapter.getValue();

        // 8. Assert that this second list *only* contains the original items ("Vixen", "Comet")
        //    and *does not* contain "Cupid".
        Assert.assertNotNull("The second list returned by getValue() should not be null.", secondListFromAdapter);
        Assert.assertEquals("The second list should still contain 2 items (original state).", 2, secondListFromAdapter.size());
        Assert.assertTrue("The second list should contain 'Vixen'.", secondListFromAdapter.contains("Vixen"));
        Assert.assertTrue("The second list should contain 'Comet'.", secondListFromAdapter.contains("Comet"));
        Assert.assertFalse("The second list should NOT contain 'Cupid'.", secondListFromAdapter.contains("Cupid"));

        // Also, verify the underlying TwinColGrid's value directly
        Set<String> twinColGridValue = twinColGrid.getValue();
        Assert.assertNotNull("The TwinColGrid's value should not be null.", twinColGridValue);
        Assert.assertEquals("The TwinColGrid's value should contain 2 items.", 2, twinColGridValue.size());
        Assert.assertTrue("The TwinColGrid's value should contain 'Vixen'.", twinColGridValue.contains("Vixen"));
        Assert.assertTrue("The TwinColGrid's value should contain 'Comet'.", twinColGridValue.contains("Comet"));
        Assert.assertFalse("The TwinColGrid's value should NOT contain 'Cupid'.", twinColGridValue.contains("Cupid"));
    }
}
