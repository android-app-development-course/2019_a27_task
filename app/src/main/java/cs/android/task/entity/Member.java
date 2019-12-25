package cs.android.task.entity;

import java.util.Objects;

/**
 * The type Member.
 * Member should have an unique phone number to identify itself.
 * This class's equal and hashcode method have been overrided.
 * If two members have same phone numbers, they should be the same one.
 * */
public class Member {

private String name;
private String phoneNum;
private String email;

  /** Instantiates a new Member. */
  public Member() {}

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
}

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
}

  /**
   * Gets phone num.
   *
   * @return the phone num
   */
  public String getPhoneNum() {
    return phoneNum;
}

  /**
   * Sets phone num.
   *
   * @param phoneNum the phone num
   */
  public void setPhoneNum(String phoneNum) {
    this.phoneNum = phoneNum;
}

  /**
   * Gets email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
}

  /**
   * Sets email.
   *
   * @param email the email
   */
  public void setEmail(String email) {
    this.email = email;
}

@Override
public boolean equals (Object o) {
    if ( this == o )
        return true;
    if ( o == null || getClass() != o.getClass() )
        return false;
    Member member = ( Member ) o;
    return Objects.equals(phoneNum,member.phoneNum) || Objects.equals(email,member.email);
}

@Override
public int hashCode () {
    return Objects.hash(phoneNum,email);
}
}
