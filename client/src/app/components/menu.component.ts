import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Menu } from '../models';
import { CartStore } from '../cart.store';
import { map, Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit{
  // TODO: Task 2
  restaurantService = inject(RestaurantService)
  cartStore = inject(CartStore)
  router = inject(Router)


  cart$ = this.cartStore.getCart()
  menuList$ : Observable<Menu[]> = this.cartStore.getMenuList()
  totalPrice$ : Observable<number> = this.cartStore.calculateTotalPrice()
  totalNumber$: Observable<number> = this.cartStore.getMenuListSize()


  ngOnInit(): void {
    this.cartStore.initialise()
    /* this.restaurantService.getMenuItems().subscribe(
      (data)=> {
        console.log(data)
        this.menuList = data
      }
    ) */ 
  }

  add(menu: Menu){
    this.cartStore.addItem(menu)
  }

  subtract(menu: Menu){
    this.cartStore.subtractItem(menu)
  }

  placeOrderInvalid(){
    let placeOrderInvalid = true
    this.totalNumber$.subscribe(
      (number) => {
        if (number > 0){
          placeOrderInvalid=false
        }
      }
    )
    return placeOrderInvalid
  }

  goToPlaceOrder(){
    this.router.navigate(['/placeOrder'])
  }

}
