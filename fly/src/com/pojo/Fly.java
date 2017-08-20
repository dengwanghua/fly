package com.pojo;

import org.eclipse.jdt.internal.compiler.apt.dispatch.RoundDispatcher;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

public class Fly {
	public int id;
	public String sale_nbr;
	public int cnt;
	public double round;
	public float cnt_rate;
	public float round_rate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSale_nbr() {
		return sale_nbr;
	}
	public void setSale_nbr(String sale_nbr) {
		this.sale_nbr = sale_nbr;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public double getRound() {
		return round;
	}
	public void setRound(double round) {
		this.round = round;
	}
	public float getCnt_rate() {
		return cnt_rate;
	}
	public void setCnt_rate(float cnt_rate) {
		this.cnt_rate = cnt_rate;
	}
	public float getRound_rate() {
		return round_rate;
	}
	public void setRound_rate(float round_rate) {
		this.round_rate = round_rate;
	}
	
	
	
}
