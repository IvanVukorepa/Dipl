using chat.Models;
using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Table;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace chat.Repositories
{
    public class UserRepository
    {
        public static void AddGroupToUser(string username, string group){
            CloudStorageAccount storageAccount = CloudStorageAccount.Parse(Environment.GetEnvironmentVariable("AzureWebJobsStorage"));
            CloudTableClient tableClient = storageAccount.CreateCloudTableClient();

            CloudTable table = tableClient.GetTableReference("ChatTable");
            table.CreateIfNotExistsAsync().Wait();

            UserGroup userGroup = new UserGroup(username, group);
            Console.WriteLine(userGroup.Username);
            Console.WriteLine(userGroup.Group);
            table.ExecuteAsync(TableOperation.InsertOrMerge(userGroup)).Wait();
        }

        public static async Task<List<UserGroup>> GetAllUserGroupsForUser(string username)
        {
            CloudStorageAccount storageAccount = CloudStorageAccount.Parse(Environment.GetEnvironmentVariable("AzureWebJobsStorage"));
            CloudTableClient tableClient = storageAccount.CreateCloudTableClient();

            CloudTable table = tableClient.GetTableReference("ChatTable");
            table.CreateIfNotExistsAsync().Wait();


            string filter = TableQuery.GenerateFilterCondition("PartitionKey", QueryComparisons.Equal, username);
            var query = new TableQuery<UserGroup>().Where(filter);

            var res = await table.ExecuteQuerySegmentedAsync(query, null);

            List<UserGroup> users = new List<UserGroup>();
            foreach(var ug in res.Results){
                users.Add(ug);
            }
            
            return users;
        }

    }
}
