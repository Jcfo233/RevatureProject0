package com.revature.instances;

public class Application {
	User user;
	private int accid;

	public Application() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Application(User user, int accid) {
		super();
		this.user = user;
		this.accid = accid;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getAccid() {
		return accid;
	}

	public void setAccid(int accid) {
		this.accid = accid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accid;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Application other = (Application) obj;
		if (accid != other.accid)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Application [" + user.toString() + ", " + ((accid == 0) ? "New Account" : accid) + "]";
	}
	
	
}
