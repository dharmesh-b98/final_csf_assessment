// You may use this file to create any models

export interface Menu{
    id: string
    name: string
    description: string
    price: number
    quantity:number
}


export interface Cart{
    menuList: Menu[]
}



export class Item{
    id!: string
    price!: number
    quantity!: number
}

export interface Order{
    username: string
    password: string
    items: Item[]
}


export interface Response{
    orderId: string
    paymentId: string
}

