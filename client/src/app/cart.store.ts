import { inject, Injectable, OnInit } from "@angular/core";
import { Cart, Menu } from "./models";
import { BehaviorSubject, map, Observable, tap } from "rxjs";
import { UntypedFormBuilder } from "@angular/forms";
import { RestaurantService } from "./restaurant.service";

// Use the following class to implement your store
@Injectable({
    providedIn: "root"
})
export class CartStore{

    restaurantService = inject(RestaurantService)
    
    private cartSubject = new BehaviorSubject<Cart>({menuList: []}) 
    cart$ = this.cartSubject.asObservable()

    paymentId = ""
    orderId = ""
    timestamp = 0
    total = 0
    
    initialise(/* initialMenuList :Observable<Menu[]> */){
        const subscription = this.restaurantService.getMenuItems().subscribe({
            next: (menuList) => {
                menuList.forEach(
                    (menu) => {
                        menu.quantity=0
                    }
                )
                this.cartSubject.next({menuList} as Cart)
            }
        })
    }

    getCart(): Observable<Cart>{
        return this.cart$
    }

    addItem(menu: Menu){ //update
        const menuList = this.cartSubject.getValue().menuList
        const indexToEdit = menuList.findIndex((a) => (a.id == menu.id));

        const newMenuList = [...menuList]
        const newMenu = menuList.at(indexToEdit) ?? {} as Menu
        newMenu!.quantity ++

        newMenuList.splice(indexToEdit,1,newMenu)
        
        this.cartSubject.next({menuList: newMenuList})
    }


    subtractItem(menu: Menu){ //update
        const menuList = this.cartSubject.getValue().menuList
        const indexToEdit = menuList.findIndex((a) => (a.id == menu.id));

        const newMenuList = [...menuList]
        const newMenu = menuList.at(indexToEdit) ?? {} as Menu

        if (newMenu.quantity != 0){
            newMenu!.quantity --
        }

        newMenuList.splice(indexToEdit,1,newMenu)
        
        this.cartSubject.next({menuList: newMenuList})
    }


    getMenuList(){
        return this.cart$.pipe(
          map((cart) => {
            return cart.menuList as Menu[]
          })  
        )
    }

    calculateTotalPrice(){
        const menuList = this.getMenuList()
        return menuList.pipe(
          map((menuList) => {
            let totalPrice = 0
            menuList.forEach(
              (menu) => {
                totalPrice += (menu.price * menu.quantity)
              }
            )
            return totalPrice as number
          })
        )
    }

    getMenuListSize(){
        const menuList = this.getMenuList()
        return menuList.pipe(
            map((menuList) => {
              let totalNumber = 0
              menuList.forEach(
                (menu) => {
                  totalNumber += (menu.quantity)
                }
              )
              return totalNumber as number
            })
        )
    }

    getOrderedMenuList(){
        return this.cart$.pipe(
            map((cart) => {
              return cart.menuList.filter(menu => (menu.quantity > 0)) as Menu[]
            })  
          )
    }
}