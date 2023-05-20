package com.nuban.nubangenerator.repository;

import com.nuban.nubangenerator.data.model.Nuban;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NubanRepo extends JpaRepository<Nuban, Long> {
}
