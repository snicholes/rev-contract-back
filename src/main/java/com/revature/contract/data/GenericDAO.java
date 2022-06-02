package com.revature.contract.data;

import java.util.Collection;
import java.util.Optional;

public interface GenericDAO<T> {
	public T save(T t);
	public Optional<T> findById(int id);
	public Collection<T> findAll();
	public T delete(T t);
}
