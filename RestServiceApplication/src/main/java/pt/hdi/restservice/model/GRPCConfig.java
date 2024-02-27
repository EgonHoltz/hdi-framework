package pt.hdi.restservice.model;


import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import pt.hdi.restservice.Utils.ApplicationEnums.FLOW_DIRECTION;

public class GRPCConfig {
	
	private String clientId;
	private FLOW_DIRECTION direction;
	private Boolean active;
	
	public GRPCConfig() {
		super();
	}

	public FLOW_DIRECTION getDirection() {
		return direction;
	}

	public void setDirection(FLOW_DIRECTION direction) {
		this.direction = direction;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return "GRPCConfig [clientId=" + clientId + ", direction=" + direction + ", active=" + active + "]";
	}

}
