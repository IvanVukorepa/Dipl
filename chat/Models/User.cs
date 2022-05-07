using Microsoft.WindowsAzure.Storage.Table;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace chat.Models
{
    public class UserGroup : TableEntity
    {
        public UserGroup(){}
        public UserGroup(string username, string group):base(username, group)
        {
            Username = username;
            Group = group;
        }

        public UserGroup(string username, string group, string chatName):base(username, group)
        {
            Username = username;
            Group = group;
            ChatName = chatName;
        }

        public string Username {get; set;}
        public string Group {get; set;}
        public string ChatName {get; set;}

    }
}
