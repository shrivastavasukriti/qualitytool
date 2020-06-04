package com.sopra.backend.qualitytool.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sopra.backend.qualitytool.entities.FileDetail;

@Repository
public interface FileDetailsRepository extends CrudRepository<FileDetail,Long>{

	Optional<FileDetail> findById(Long id);
}
