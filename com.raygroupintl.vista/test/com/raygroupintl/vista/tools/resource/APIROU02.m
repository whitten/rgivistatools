APIROU02 ;AU - API TEST ; JUN 18,2012
 ;;3.0;TESTING;;JUN 18,2012
 ;
 S (NE,HR)=1
 W ME
 Q
 ;
FACT(N) ;
 N R
 S R=1
 F I=1:1:N S R=R*I
 S M=R
 Q R
 ;
