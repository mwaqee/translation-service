package org.mwtech.translation.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="key_tags")
@IdClass(KeyTag.KeyTagId.class)
public class KeyTag {
	@Id
	@Column(name="key_id")
	private Long keyId;
  
	@Id
	@Column(name="tag_id")
	private Long tagId;

	public KeyTag() {}
  
	public KeyTag(Long keyId, Long tagId){ 
		this.keyId=keyId; this.tagId=tagId;
	}

	public Long getKeyId() { return keyId; }
	public void setKeyId(Long keyId) { this.keyId = keyId; }

	public Long getTagId() { return tagId; }
	public void setTagId(Long tagId) { this.tagId = tagId; }

	public static class KeyTagId implements Serializable {
	    public Long keyId;
	    public Long tagId;
	    
	    public KeyTagId() {}
	    
	    public KeyTagId(Long k, Long t){ keyId=k; tagId=t; }
	    
	    @Override
	    public boolean equals(Object o){
	      if(this==o) return true;
	      
	      if(!(o instanceof KeyTagId k)) return false;
	      
	      return Objects.equals(keyId,k.keyId) && Objects.equals(tagId,k.tagId);
	    }
	    
	    @Override
	    public int hashCode(){ return Objects.hash(keyId,tagId); }
	}
}
