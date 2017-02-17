package net.orekyuu.gitthrow.theme.port;

import net.orekyuu.gitthrow.theme.domain.model.ThemeImage;
import net.orekyuu.gitthrow.theme.port.table.ProjectThemeDao;
import net.orekyuu.gitthrow.theme.port.table.ProjectThemeTable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProjectThemeImageRepository {

    private final ProjectThemeDao themeDao;

    public ProjectThemeImageRepository(ProjectThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public void save(String projectId, ThemeImage img) {
        themeDao.findByProject(projectId).map(table -> {
            if (img.getImg() == null) {
                return themeDao.saveOpacity(new ProjectThemeTable(projectId, img.getOpacity(), null, img.getLastModified()));
            }
            return themeDao.save(new ProjectThemeTable(projectId, img.getOpacity(), img.getImg(), img.getLastModified()));
        }).orElseGet(() -> themeDao.insert(new ProjectThemeTable(projectId, img.getOpacity(), img.getImg(), img.getLastModified())));
    }

    public Optional<ThemeImage> findByProject(String projectId) {
        return themeDao.findByProject(projectId).map(it -> new ThemeImage(it.getImage(), it.getOpacity(), it.getUpdatedAt()));
    }

    public void deleteByProject(String projectId) {
        themeDao.deleteByProject(projectId);
    }


}
