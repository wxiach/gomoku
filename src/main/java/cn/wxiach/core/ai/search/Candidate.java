package cn.wxiach.core.ai.search;

import cn.wxiach.model.Point;

public class Candidate implements Comparable<Candidate> {

    private final Point point;
    private int score;

    public Candidate(Point point) {
        this(point, 0);
    }

    public Candidate(Point point, int score) {
        this.point = point;
        this.score = score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Point getPoint() {
        return point;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Candidate o) {
        return Integer.compare(this.getScore(), o.getScore());
    }
}
