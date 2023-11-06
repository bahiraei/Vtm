package ir.adak.Vect;

public class Music {
    String Name;
    String Name_S;
    int music;
    int Img;

    public Music(String name, String name_S, int music, int img) {
        Name = name;
        Name_S = name_S;
        this.music = music;
        Img = img;
    }

    public int getMusic() {
        return music;
    }

    public void setMusic(int music) {
        this.music = music;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName_S() {
        return Name_S;
    }

    public void setName_S(String name_S) {
        Name_S = name_S;
    }

    public int getImg() {
        return Img;
    }

    public void setImg(int img) {
        Img = img;
    }
}
