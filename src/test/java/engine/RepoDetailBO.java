package engine;


import engine.entity.RepoRestrictDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepoDetailBO extends RepoRestrictDetail {
    private List<RepoGroupBO> repoGroupBOS;
}
