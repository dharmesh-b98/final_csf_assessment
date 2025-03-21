import { Component, inject, OnInit } from '@angular/core';
import { CartStore } from '../cart.store';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Item, Menu, Order } from '../models';
import { RestaurantService } from '../restaurant.service';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit{

  cartStore = inject(CartStore)
  fb = inject(FormBuilder)
  router = inject(Router)
  restaurantService = inject(RestaurantService)

  form!: FormGroup
  orderedMenuList$ = this.cartStore.getOrderedMenuList()
  totalPrice$ = this.cartStore.calculateTotalPrice()


  ngOnInit(): void {
    this.form = this.createForm()
  }


  createForm(){
    return this.fb.group({
      "username": this.fb.control<string>("",[]),
      "password": this.fb.control<string>("",[])
    })
  }


  goToMenu(){
    this.router.navigate(['/'])
  }


  isFormInvalid(){
    if (this.form.value.username == "" || this.form.value.password == ""){
      return true
    }
    return false
  }


  submitForm(){
    const order: Order = this.getOrder(this.form.value.username, this.form.value.password)
    this.restaurantService.postOrder(order).subscribe({
      next: (data) => {
        console.log(data)
        this.cartStore.orderId=data.orderId
        this.cartStore.paymentId = data.paymentId
        this.cartStore.timestamp = data.timestamp
        this.cartStore.total = data.total
        this.router.navigate(['/confirm'])
      },
      error: (error)=> {
        alert(error.message)
      }
      
    })
  }


  getOrder(username: string, password: string): Order{
    let orderedMenuList = [] as Menu[];
    this.orderedMenuList$.subscribe(
      (data) => {
        orderedMenuList = data
      }
    )
    
    let items = [] as Item[]
    for (let orderedMenu of orderedMenuList){
      let item: Item = {} as Item
      item.id = orderedMenu.id
      item.price = orderedMenu.price
      item.quantity = orderedMenu.quantity
      items.push(item)
    }

    let user: Order = {} as Order
    user.username = username
    user.password = password
    user.items = items
    
    return user
  } 

}
