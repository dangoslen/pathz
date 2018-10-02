package me.dangoslen.pathz.api;

import me.dangoslen.pathz.models.TeamMate;
import me.dangoslen.pathz.repository.TeamMatesRepository;
import me.dangoslen.pathz.utils.PhoneUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/teammates")
public class TeamMatesController {

    private final TeamMatesRepository teamMatesRepository;

    @Autowired
    public TeamMatesController(TeamMatesRepository teamMatesRepository) {
        this.teamMatesRepository = teamMatesRepository;
    }

    @GetMapping
    public ResponseEntity<Collection<TeamMate>> getTeamMates() {
        return ResponseEntity.ok(teamMatesRepository.getTeamMates());
    }

    @PostMapping
    public ResponseEntity<Void> createTeamMate(@RequestBody TeamMate teamMate, UriComponentsBuilder uriComponentsBuilder) {
        teamMate.setPhoneNumber(PhoneUtils.getFormattedNumber(teamMate.getPhoneNumber()));
        teamMatesRepository.saveTeamMate(teamMate);
        UriComponents components = uriComponentsBuilder.path("/teammates/{handle}").buildAndExpand(teamMate.getHandle());
        return ResponseEntity.created(components.toUri()).build();
    }

    @GetMapping("/{handle}")
    public ResponseEntity<TeamMate> getTeamMate(@PathVariable("handle") String handle) {
        Optional<TeamMate> teamMate = teamMatesRepository.getTeamMate(handle);

        if (teamMate.isPresent()) {
            return ResponseEntity.ok(teamMate.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
