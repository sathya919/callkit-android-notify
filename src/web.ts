import { WebPlugin } from '@capacitor/core';

import type { callkitandroidPlugin } from './definitions';

export class callkitandroidWeb extends WebPlugin implements callkitandroidPlugin {
 async register(): Promise<void> {
    return;
  }

  async incomingCall({from}:{from:string}):Promise<void>{
    console.log(from)
    return;
  }
}
