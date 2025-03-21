import { Component, inject } from '@angular/core';
import { CartStore } from '../cart.store';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent {

  // TODO: Task 5
  cartStore = inject(CartStore)
  
  orderId = this.cartStore.orderId
  paymentId = this.cartStore.paymentId
  timestamp = new Date(this.cartStore.timestamp)
  total = this.cartStore.total
  

}
