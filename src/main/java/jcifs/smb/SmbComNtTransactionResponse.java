/* jcifs smb client library in Java
 * Copyright (C) 2005  "Michael B. Allen" <jcifs at samba dot org>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package jcifs.smb;


import org.apache.log4j.Logger;

import jcifs.Configuration;


abstract class SmbComNtTransactionResponse extends SmbComTransactionResponse {

    private static final Logger log = Logger.getLogger(SmbComNtTransactionResponse.class);


    SmbComNtTransactionResponse ( Configuration config ) {
        super(config);
    }


    @Override
    int readParameterWordsWireFormat ( byte[] buffer, int bufferIndex ) {
        int start = bufferIndex;

        buffer[ bufferIndex++ ] = (byte) 0x00; // Reserved
        buffer[ bufferIndex++ ] = (byte) 0x00; // Reserved
        buffer[ bufferIndex++ ] = (byte) 0x00; // Reserved

        this.totalParameterCount = SMBUtil.readInt4(buffer, bufferIndex);
        if ( this.bufDataStart == 0 ) {
            this.bufDataStart = this.totalParameterCount;
        }
        bufferIndex += 4;
        this.totalDataCount = SMBUtil.readInt4(buffer, bufferIndex);
        bufferIndex += 4;
        this.parameterCount = SMBUtil.readInt4(buffer, bufferIndex);
        bufferIndex += 4;
        this.parameterOffset = SMBUtil.readInt4(buffer, bufferIndex);
        bufferIndex += 4;
        this.parameterDisplacement = SMBUtil.readInt4(buffer, bufferIndex);
        bufferIndex += 4;
        this.dataCount = SMBUtil.readInt4(buffer, bufferIndex);
        bufferIndex += 4;
        this.dataOffset = SMBUtil.readInt4(buffer, bufferIndex);
        bufferIndex += 4;
        this.dataDisplacement = SMBUtil.readInt4(buffer, bufferIndex);
        bufferIndex += 4;
        this.setupCount = buffer[ bufferIndex ] & 0xFF;
        bufferIndex += 2;
        if ( this.setupCount != 0 ) {
            if ( log.isDebugEnabled() )
                log.debug("setupCount is not zero: " + this.setupCount);
        }

        return bufferIndex - start;
    }
}
