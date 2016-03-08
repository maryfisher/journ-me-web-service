package com.journme.rest.common.searchfilter;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 07.03.2016
 */
public class NoteSearchFilter extends SearchFilter {

    String notebookId;

    public String getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(String notebookId) {
        this.notebookId = notebookId;
    }
}
