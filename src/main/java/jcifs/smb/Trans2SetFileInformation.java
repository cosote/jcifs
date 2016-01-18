/* jcifs smb client library in Java
 * Copyright (C) 2003  "Michael B. Allen" <jcifs at samba dot org>
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


import jcifs.Configuration;


class Trans2SetFileInformation extends SmbComTransaction {

    static final int SMB_FILE_BASIC_INFO = 0x101;

    private int fid;
    private int attributes;
    private long createTime, lastWriteTime;


    Trans2SetFileInformation ( Configuration config, int fid, int attributes, long createTime, long lastWriteTime ) {
        super(config);
        this.fid = fid;
        this.attributes = attributes;
        this.createTime = createTime;
        this.lastWriteTime = lastWriteTime;
        this.command = SMB_COM_TRANSACTION2;
        this.subCommand = TRANS2_SET_FILE_INFORMATION;
        this.maxParameterCount = 6;
        this.maxDataCount = 0;
        this.maxSetupCount = (byte) 0x00;
    }


    @Override
    int writeSetupWireFormat ( byte[] dst, int dstIndex ) {
        dst[ dstIndex++ ] = this.subCommand;
        dst[ dstIndex++ ] = (byte) 0x00;
        return 2;
    }


    @Override
    int writeParametersWireFormat ( byte[] dst, int dstIndex ) {
        int start = dstIndex;

        SMBUtil.writeInt2(this.fid, dst, dstIndex);
        dstIndex += 2;
        SMBUtil.writeInt2(SMB_FILE_BASIC_INFO, dst, dstIndex);
        dstIndex += 2;
        SMBUtil.writeInt2(0, dst, dstIndex);
        dstIndex += 2;

        return dstIndex - start;
    }


    @Override
    int writeDataWireFormat ( byte[] dst, int dstIndex ) {
        int start = dstIndex;

        SMBUtil.writeTime(this.createTime, dst, dstIndex);
        dstIndex += 8;
        SMBUtil.writeInt8(0L, dst, dstIndex);
        dstIndex += 8;
        SMBUtil.writeTime(this.lastWriteTime, dst, dstIndex);
        dstIndex += 8;
        SMBUtil.writeInt8(0L, dst, dstIndex);
        dstIndex += 8;
        /*
         * Samba 2.2.7 needs ATTR_NORMAL
         */
        SMBUtil.writeInt2(0x80 | this.attributes, dst, dstIndex);
        dstIndex += 2;
        /* 6 zeros observed with NT */
        SMBUtil.writeInt8(0L, dst, dstIndex);
        dstIndex += 6;

        /*
         * Also observed 4 byte alignment but we stick
         * with the default for jCIFS which is 2
         */

        return dstIndex - start;
    }


    @Override
    int readSetupWireFormat ( byte[] buffer, int bufferIndex, int len ) {
        return 0;
    }


    @Override
    int readParametersWireFormat ( byte[] buffer, int bufferIndex, int len ) {
        return 0;
    }


    @Override
    int readDataWireFormat ( byte[] buffer, int bufferIndex, int len ) {
        return 0;
    }


    @Override
    public String toString () {
        return new String("Trans2SetFileInformation[" + super.toString() + ",fid=" + this.fid + "]");
    }
}
