package com.ssafy.pjt1.model.dto.vote;

public class VoteDto {

    private int vote_id;
    private int board_id;
    private String vote_name;
    private String vote_description;

    public int getVote_id() {
        return this.vote_id;
    }

    public void setVote_id(int vote_id) {
        this.vote_id = vote_id;
    }

    public int getBoard_id() {
        return this.board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public String getVote_name() {
        return this.vote_name;
    }

    public void setVote_name(String vote_name) {
        this.vote_name = vote_name;
    }

    public String getVote_description() {
        return this.vote_description;
    }

    public void setVote_description(String vote_description) {
        this.vote_description = vote_description;
    }

    public VoteDto(){
        super();
    }
}
