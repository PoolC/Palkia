package poolc.poolc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import poolc.poolc.domain.Tag;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepository {
    private final EntityManager em;

    public void save(Tag tag){
        em.persist(tag);
    }

    public void delete(Long id){
        em.remove(em.find(Tag.class,id));
    }

    public Tag findOne(Long tagID){
        return em.find(Tag.class, tagID);
    }

    public List<Tag> findAll(){
        return em.createQuery("select t from Tag t")
                .getResultList();
    }

}
