export class Connected {
    public position: string;
    public userName: string;
    public role: string;
}

export function convertObjectToConnected(data: Connected): Connected {
    const connected: Connected = new Connected();
    connected.userName = data.userName;
    connected.role = data.role;
    connected.position = data.position;
    return connected;
}
