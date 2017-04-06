import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class WebSocketWSService {
    private ws: WebSocket;

    public createWebSocket(url: string): Observable<string> {
        this.ws = new WebSocket(url);
        return new Observable(
            (observer) => {
                this.ws.onmessage = (event) => observer.next(event.data);
                this.ws.onerror = (event) => observer.error(event);
                this.ws.onclose = (event) => observer.complete();
            }
        );
    }

    public sendMessage(message: any): void {
        if (this.ws) {
            this.ws.send(message);
        }
    }
}
