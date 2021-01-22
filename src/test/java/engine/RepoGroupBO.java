package engine;


import engine.entity.RepoObjectGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepoGroupBO extends RepoObjectGroup {
    private List<RepoObjectBO> repoObjectBOs;

}
