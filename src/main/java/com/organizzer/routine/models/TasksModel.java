package com.organizzer.routine.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity(name="tasks")
@Table(name="tasks")
public class TasksModel {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="task_id") 
	private Integer taskId;
	
	@Column(name="fk_user_id")
	@JoinColumn(name="user_id")
	private Integer fkUserId;
	
	@Column(name="task_name")
	private String taskName;
	
	@Column(name="task_details")
	private String taskDetails;
	
	@Column(name="task_group")
	private String taskGroup;
	
	@Column(name="task_image_group")
	private String taskImageGroup;
	
	
	
	

	public TasksModel(Integer taskId, Integer fkUserId, String taskName, String taskDetails, String taskGroup,
			String taskImageGroup) {
		super();
		this.taskId = taskId;
		this.fkUserId = fkUserId;
		this.taskName = taskName;
		this.taskDetails = taskDetails;
		this.taskGroup = taskGroup;
		this.taskImageGroup = taskImageGroup;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getFkUserId() {
		return fkUserId;
	}

	public void setFkUserId(Integer fkUserId) {
		this.fkUserId = fkUserId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDetails() {
		return taskDetails;
	}

	public void setTaskDetails(String taskDetails) {
		this.taskDetails = taskDetails;
	}

	public String getTaskGroup() {
		return taskGroup;
	}

	public void setTaskGroup(String taskGroup) {
		this.taskGroup = taskGroup;
	}

	public String getTaskImageGroup() {
		return taskImageGroup;
	}

	public void setTaskImageGroup(String taskImageGroup) {
		this.taskImageGroup = taskImageGroup;
	}
	
	
	
	
}
