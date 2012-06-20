<?php defined('_JEXEC') or die('Restricted access'); ?>

<form action="index.php" method="post" name="adminForm" id="adminForm">
<div class="col100">
	<fieldset class="adminform">
		<legend><?php echo JText::_( 'Pls fill in Order Details' ); ?></legend>

		<table class="admintable">
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Subscriber' ); ?>:
				</label>
			</td>
			<td>
				<?php 
				$options = array();
 
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A SUBSCRIBER'));
    //dump($this->subscribers, "SUBS");
    foreach($this->subscribers as $key=>$value) :
      $options[] = JHTML::_('select.option', $value->sub_code, JText::_($value->sub_name.'('.$value->sub_code.')'));
    endforeach;
    $selected = ($this->subcode > 0)? $this->subcode : '';
    echo JHTML::_('select.genericlist', $options, 'sub_code', '', 'value', 'text', $selected);
				?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Order Code' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="order_code" id="order_code" size="32" maxlength="250" value="" />
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Order Date' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="order_date" id="order_date" size="32" maxlength="250" value="<?php echo $this->order->order_date;?>" />
				<input type="reset" class="button" value="..." onclick="return showCalendar('order_date','%Y-%m-%d');">
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Order Title' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="order_title" id="order_title" size="32" maxlength="250" value="<?php echo $this->order->order_title;?>" />
			</td>
		</tr>
		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Invoice Number' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="order_invno" id="order_invno" size="32" maxlength="250" value="<?php echo $this->order->order_invno;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Invoice Amount' ); ?>:
				</label>
			</td>
			<td>
			<input class="textarea" type="text" name="order_invamt" id="order_invamt" size="32" maxlength="250" value="<?php echo $this->order->order_invamt;?>" />				
			</td>
		</tr>		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Amount Paid?' ); ?>:
				</label>
			</td>
			<td>
			  <?php 
				$optionsPaid = array(JHTML::_('select.option','',JText::_('PLS CHOOSE OPTION')),

				  JHTML::_('select.option', 'Yes', JText::_('Yes')),				  
				  JHTML::_('select.option', 'No', JText::_('No'))				  
				);
    echo JHTML::_('select.genericlist', $optionsPaid, 'order_paid', 'class="inputbox"', 'value', 'text', $this->order->order_paid);
				?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>			
		</tr>		
		
		<tr>
		  <table class="adminlist">
	    <thead>
		<tr>		
			<th>
				<?php echo JText::_( 'Publication' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Subscription Title' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Issues' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Copies' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Issue from' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Issue To' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Start Date' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Exp Date' ); ?>
			</th>				
		</tr>
	</thead>
			<?php
	$k = 0;
	for ($i=0, $n=2; $i < $n; $i++)	{	
		?>
		<tr class="<?php echo "row$k"; ?>">					
			<td>
			<?php 
				$options = array();
 
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A PUBLICATION'));
    foreach($this->publications as $key=>$value) :
      ## Create $value ##
      #dump($value, "CATS");
      $options[] = JHTML::_('select.option', $value->pub_code, JText::_($value->pub_name.'('.$value->pub_code.')'));
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'pub_code'.$i, '', 'value', 'text', '');
				?>
			</td>
			<td>
			<input class="inputbox" type="text" name="subscription_title<?php echo $i; ?>" id="subscription_title<?php echo $i; ?>" size="15" maxlength="250" value="" />				
			</td>
			<td>
			<input class="inputbox" type="text" name="num_issues<?php echo $i; ?>" id="num_issues<?php echo $i; ?>" size="8" maxlength="250" value="" />				
			</td>				
			<td>
			<input class="inputbox" type="text" name="num_copies<?php echo $i; ?>" id="num_copies<?php echo $i; ?>" size="8" maxlength="250" value="" />			
			</td>
			<td>
		<input class="inputbox" type="text" name="issue_from<?php echo $i; ?>" id="issue_from<?php echo $i; ?>" size="10" maxlength="250" value="" />		
			</td>
			<td>
				<input class="inputbox" type="text" name="issue_to<?php echo $i; ?>" id="issue_to<?php echo $i; ?>" size="10" maxlength="250" value="" />	
			</td>
			<td>
			<input class="textarea" type="text" name="start_date<?php echo $i; ?>" id="start_date<?php echo $i; ?>" size="15" maxlength="250" value="" />
				<input type="reset" class="button" value="..." onclick="return showCalendar('start_date<?php echo $i; ?>','%Y-%m-%d');">				
			</td>
			<td>
			<input class="textarea" type="text" name="exp_date<?php echo $i; ?>" id="exp_date<?php echo $i; ?>" size="15" maxlength="250" value="" />
				<input type="reset" class="button" value="..." onclick="return showCalendar('exp_date<?php echo $i; ?>','%Y-%m-%d');">		
			</td>			
		</tr>
		<?php
		$k = 1 - $k;
	}
	?>	
  </table>
		</tr>
		
	</table>
	</fieldset>
</div>
<div class="clr"></div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="controller" value="order" />
</form>
