package cs.android.task.entity;

import java.util.Date;
import java.util.Objects;

public class LogItem {

    private String content;
    private Date date;
    private Member commiter;

    public Member getCommiter() {
        return commiter;
    }

    public void setCommiter(Member commiter) {
        this.commiter = commiter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LogItem logItem = (LogItem) o;
        return date.equals(logItem.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
