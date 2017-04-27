import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class WebSocketWSService {
    private ws: WebSocket;
    private queued: string[] = new Array<string>();

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
            if (this.ws && this.ws.readyState === this.ws.OPEN) {
                this.ws.send(JSON.stringify(message));
            } else if (this.ws.readyState === this.ws.CONNECTING) {
                this.queued.push(JSON.stringify(message));
                setTimeout(() => {
                    this.waitForConnected();
                }, 500);
            }
        }
    }

    private waitForConnected(): void {
        if (this.ws && this.ws.readyState === this.ws.OPEN) {
            while (this.queued.length > 0) {
                const message: string = this.queued.shift();
                this.ws.send(message);
            }
        } else if (this.ws.readyState === this.ws.CONNECTING) {
            setTimeout(() => {
                this.waitForConnected();
            }, 500);
        }
    }
}
