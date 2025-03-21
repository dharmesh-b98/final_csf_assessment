import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Menu, Order } from "./models";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root"
})
export class RestaurantService {

  http = inject(HttpClient)
  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems() : Observable<Menu[]> {
    return this.http.get<Menu[]>("/api/menu")
  }

  // TODO: Task 3.2

  postOrder(order: Order): Observable<any>{
    console.log(JSON.stringify(order))
    return this.http.post<any>("/api/food_order", JSON.stringify(order))
  }
  
}
