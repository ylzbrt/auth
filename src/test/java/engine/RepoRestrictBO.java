package engine;


import engine.entity.RepoRestrict;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepoRestrictBO  extends RepoRestrict {
    private List<RepoDetailBO> repoDetailBOs;
    private List<RepoAttributeBO> repoAttributeBOs;
}
