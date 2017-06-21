package cn.cookiemouse.onenote.data;

/**
 * Created by cookiemouse on 17-6-21.
 */

public class NoteListData {
    private String text, path;
    private int type, grade;
    private boolean state;
//    private String remarks;   备注信息，暂定不实现

    public NoteListData() {
        this.text = "";
        this.path = "";
        this.type = DataType.TYPE_PERSONAL;
        this.grade = DataGrade.GRADE_NORMAL;
        this.state = DataState.STATE_OFF;
    }

    public NoteListData(String text, String path) {
        this();
        this.text = text;
        this.path = path;
    }

    public NoteListData(String text, String path, int type, int grade) {
        this(text, path);
        this.type = type;
        this.grade = grade;
    }

    public NoteListData(String text, String path, int type, int grade, boolean state) {
        this(text, path, type, grade);
        this.state = state;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
