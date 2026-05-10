package com.riadh.ecommerce.features.file.repository;

import com.riadh.ecommerce.features.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileResponseRepository extends JpaRepository<File, Long> {
}