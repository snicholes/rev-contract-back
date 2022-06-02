package com.revature.contract.data;

import java.util.Optional;

import com.revature.contract.models.Associate;

public interface AssociateDAO extends GenericDAO<Associate> {
	public Optional<Associate> findBySecretCode(String code);
}
