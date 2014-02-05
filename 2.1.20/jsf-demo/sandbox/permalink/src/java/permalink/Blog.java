/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package permalink;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@RequestScoped
@ManagedBean
public class Blog {

    private static final int PAGE_SIZE = 3;
    private Long entryId;
    private String category;
    private BlogEntry entry;
    private List<BlogEntry> entriesForPage;
    private List<String> categories;
    private boolean nextPageAvailable;
    private int page = 1;
    private String searchString;

    // --------------------------------------------------------- Injections

    @ManagedProperty("#{blogEntryRepository}")
    private BlogEntryRepository repository;

    public void setRepository(BlogEntryRepository repository) {
        this.repository = repository;
    }

    // --------------------------------------------------------- Properties

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSearchString() {
        // return null to prevent page param from being encoded when not necessary
        return searchString == null || searchString.length() == 0
               ? null
               : searchString;
    }

    public void setSearchString(String searchString) {
        if (searchString != null) {
            searchString = searchString.trim();
        }
        this.searchString = searchString;
    }

    public Integer getPage() {
        // return null to prevent page param from being encoded when not necessary
        return page == 1 ? null : page;
    }

    public void setPage(Integer page) {
        // NOTE if we were to use a primitive propery, page parameters would fail if value is null
        if (page == null) {
            this.page = 1;
        } else if (page < 1) {
            throw new IllegalArgumentException(
                  "Page must be greater than or equal to 1");
        } else {
            this.page = page;
        }
    }

    // --------------------------------------------------------- Before render actions

    public void loadCategories() {
        categories = retrieveCategories();
    }

    /**
     * Init method for the main blog page
     */
    public void loadLatestEntries() {
        loadCategories();
        entriesForPage = (searchString != null
                          ? retrieveSearchResults()
                          : retrieveLatestEntries());
    }

    /**
     * Init method for a category page
     */
    public void loadLatestEntriesInCategory() {
        loadCategories();
        if (categories.contains(category)) {
            entriesForPage = (searchString != null
                              ? retrieveSearchResultsInCategory()
                              : retrieveLatestEntriesInCategory());
        } else {
            category = null;
        }
    }

    /**
     * Init method for an entry page
     */
    public void loadEntry() {
        loadCategories();
        entry = retrieveSelectedEntry();
    }

    // --------------------------------------------------------- Command actions

    public boolean search() {
        page = 1;
        return true;
    }

    // --------------------------------------------------------- State readers

    /**
     * Retrieves the catagories used in this blog. This method references preloaded
     * data and is intended to be used in the EL value expressions in the view template.
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * Retrieves the categories used in this blog other than the selected one. This
     * method references preloaded data and is intended to be used in the EL value
     * expressions in the view template.
     */
    public List<String> getOtherCategories() {
        List<String> others = new ArrayList<String>();
        // defensive here because of postback decodes grrr..
        if (categories != null) {
            others.addAll(categories);
            others.remove(category);
        }
        return others;
    }

    /**
     * Retrieves the entries loaded for this page. This method references preloaded
     * data and is intended to be used in EL value expressions in the view template.
     */
    public List<BlogEntry> getEntriesForPage() {
        return entriesForPage;
    }

    public Set<BlogEntry> getEntriesForPageAsSet() {
        return new LinkedHashSet(entriesForPage);
    }

    public int getNumEntriesOnPage() {
        return entriesForPage.size();
    }

    /**
     * Retrieves the entry loaded for this page. This method references preloaded
     * data and is intended to be used in EL value expressions in the view template.
     */
    public BlogEntry getEntry() {
        return entry;
    }

    public int getPreviousPageWithFirstPageAsNumber() {
        assert page > 1;
        return page - 1;
    }

    public Integer getPreviousPage() {
        assert page > 1;
        return page > 2 ? page - 1 : null;
    }

    public int getPageWithFirstPageAsNumber() {
        return page;
    }

    public int getNextPage() {
        return page + 1;
    }

    public boolean isNextPageAvailable() {
        return nextPageAvailable;
    }

    public boolean isPreviousPageAvailable() {
        return page > 1;
    }

    // --------------------------------------------------------- Worker methods

    protected List<BlogEntry> retrieveLatestEntries() {
        List<BlogEntry> entries = repository
              .getLatestEntries((page - 1) * PAGE_SIZE, PAGE_SIZE + 1);
        if (entries.isEmpty() && page > 1) {
            page = 1;
            entries = repository.getLatestEntries(0, PAGE_SIZE + 1);
        }

        return postProcessNavigationProbe(entries);
    }

    protected List<BlogEntry> retrieveSearchResults() {
        List<BlogEntry> entries = repository.searchEntries(searchString,
                                                           (page - 1)
                                                           * PAGE_SIZE,
                                                           PAGE_SIZE + 1);
        if (entries.isEmpty() && page > 1) {
            page = 1;
            entries = repository.searchEntries(searchString, 0, PAGE_SIZE + 1);
        }

        return postProcessNavigationProbe(entries);
    }

    protected List<BlogEntry> retrieveLatestEntriesInCategory() {
        List<BlogEntry> entries = repository.getLatestEntries(category,
                                                              (page - 1)
                                                              * PAGE_SIZE,
                                                              PAGE_SIZE + 1);
        if (entries.isEmpty() && page > 1) {
            page = 1;
            entries = repository.getLatestEntries(category, 0, PAGE_SIZE + 1);
        }

        return postProcessNavigationProbe(entries);
    }

    protected List<BlogEntry> retrieveSearchResultsInCategory() {
        List<BlogEntry> entries = repository.searchEntries(searchString,
                                                           category,
                                                           (page - 1)
                                                           * PAGE_SIZE,
                                                           PAGE_SIZE + 1);
        if (entries.isEmpty() && page > 1) {
            page = 1;
            entries = repository
                  .searchEntries(searchString, category, 0, PAGE_SIZE + 1);
        }

        return postProcessNavigationProbe(entries);
    }

    private List<BlogEntry> postProcessNavigationProbe(List<BlogEntry> entries) {
        if (entries.size() > PAGE_SIZE) {
            nextPageAvailable = true;
            entries.remove(entries.size() - 1);
        } else {
            nextPageAvailable = false;
        }

        return entries;
    }

    protected BlogEntry retrieveSelectedEntry() {
        return repository.getEntry(entryId);
    }

    protected List<String> retrieveCategories() {
        return repository.getCategories();
    }

}
