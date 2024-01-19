#include<stdio.h>
#include<ctype.h>

int main(void)
{
	char ch[102]="NULL";
	int i=0;
	
	scanf("%s",ch);
	
	for(i=0;i<101;i++)
	{
		if(ch[i]<='z'&&ch[i]>='a') printf("%c",ch[i]);
		else if(ch[i]<='Z'&&ch[i]>='A') printf("%c",ch[i]);
		else if(ch[i]<='9'&&ch[i]>='0') printf("%c",ch[i]);
		
		else if(ch[i]=='-'){
			if(isdigit(ch[i-1])&&isdigit(ch[i+1])&&(ch[i-1]<ch[i+1]))
			{
				for(char s=ch[i-1]+1;s<ch[i+1];s++)
				{
					printf("%c",s);
				}
			}
			
			else if(islower(ch[i-1])&&islower(ch[i+1])&&(ch[i-1]<ch[i+1]))
			{
				for(char s=ch[i-1]+1;s<ch[i+1];s++)
				{
					printf("%c",s);
				}
			}
			
			else if(isupper(ch[i-1])&&isupper(ch[i+1])&&(ch[i-1]<ch[i+1]))
			{
				for(char s=ch[i-1]+1;s<ch[i+1];s++)
				{
					printf("%c",s);
				}
			}
			
			else {
				printf("-");
			}
			
		}
	}
	
	return 0;
}