package pl.coderslab.sportsbetting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.sportsbetting.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findCartByUser_Id(Long id);

}